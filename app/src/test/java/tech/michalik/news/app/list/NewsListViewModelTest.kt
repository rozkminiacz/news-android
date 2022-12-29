package tech.michalik.news.app.list

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import tech.michalik.news.app.StateFlowTurbineViewRobot
import tech.michalik.news.app.TestDispatcherFacade
import tech.michalik.news.app.displayable.ArticleEntityToDisplayableMapper
import tech.michalik.news.app.fakeArticles
import tech.michalik.news.domain.AppError
import timber.log.Timber

/**
 * Created by jaroslawmichalik
 */
class NewsListViewModelTest : BehaviorSpec({

  val dispatcher = StandardTestDispatcher()

  Given("non-empty default query") {

    val DEFAULT_INITIAL_QUERY = "Tesla"

    val viewModel = NewsListViewModel(
      dispatcherFacade = TestDispatcherFacade(dispatcher, dispatcher),
      fetchNewsUseCase = mockk {
        coEvery {
          fetchData(any())
        } throws AppError.Unknown
      },
      displayableMapper = ArticleEntityToDisplayableMapper(),
      resolveDefaultSearchQuery = mockk {
        every { fetchDefaultQuery() } returns DEFAULT_INITIAL_QUERY
      }
    )

    val robot = StateFlowTurbineViewRobot(
      scheduler = dispatcher.scheduler,
      stateFlow = viewModel.state,
    )
    When("start") {
      robot.collect()
      Then("start with default query") {
        robot.listOfStates.last().searchQuery shouldBe DEFAULT_INITIAL_QUERY
      }
    }
  }

  Given("error for empty query") {
    val viewModel = NewsListViewModel(
      dispatcherFacade = TestDispatcherFacade(dispatcher, dispatcher),
      fetchNewsUseCase = mockk {
        coEvery {
          fetchData(any())
        } throws AppError.Unknown
      },
      displayableMapper = ArticleEntityToDisplayableMapper(),
      resolveDefaultSearchQuery = mockk {
        every { fetchDefaultQuery() } returns ""
      }
    )

    val robot = StateFlowTurbineViewRobot(
      scheduler = dispatcher.scheduler,
      stateFlow = viewModel.state,
    )

    When("start") {
      repeat(2) {
        robot.collect()
      }
      Then("show state initial with loading and then state error") {
        assertSoftly {
          robot.listOfStates[0].loading shouldBe true
          robot.listOfStates[0].error shouldBe null
          robot.listOfStates[1].error shouldBe AppError.Unknown
          robot.listOfStates[1].loading shouldBe false
        }
      }
    }

    robot.close()
  }

  Given("valid articles for empty query and tesla query") {

    val viewModel = NewsListViewModel(
      dispatcherFacade = TestDispatcherFacade(dispatcher, dispatcher),
      fetchNewsUseCase = mockk {
        coEvery {
          fetchData("")
        } returns fakeArticles(4)
        coEvery {
          fetchData("tesla")
        } returns fakeArticles(11)
      },
      displayableMapper = ArticleEntityToDisplayableMapper(),
      resolveDefaultSearchQuery = mockk {
        every { fetchDefaultQuery() } returns ""
      }
    )

    val robot = StateFlowTurbineViewRobot(
      scheduler = dispatcher.scheduler,
      stateFlow = viewModel.state,
    )

    When("start") {

      robot.collect() // initial value

      robot.collect() // value for empty query

      Then("show data on UI") {
        assertSoftly {
          robot.listOfStates shouldHaveSize 2
          robot.listOfStates.last().pageDisplayable shouldHaveSize 4
        }
      }
    }

    When("change query to tesla") {
      robot.collect() // initial state

      robot.collect() // empty query load state

      viewModel.search("tesla")

      repeat(3) {
        robot.collect() // collect all expected states after search
      }

      Then("show tesla data on UI") {
        assertSoftly {
          robot.listOfStates shouldHaveSize 5
          robot.listOfStates.last().pageDisplayable shouldHaveSize 11
        }
      }
    }

    robot.close()
  }

  Given("error on empty query, valid on tesla query") {
    val viewModel = NewsListViewModel(
      dispatcherFacade = TestDispatcherFacade(dispatcher, dispatcher),
      fetchNewsUseCase = mockk {
        coEvery {
          fetchData("")
        } throws AppError.Unknown
        coEvery {
          fetchData("tesla")
        } returns fakeArticles(4)
      },
      displayableMapper = ArticleEntityToDisplayableMapper(),
      resolveDefaultSearchQuery = mockk {
        every { fetchDefaultQuery() } returns ""
      }
    )

    val robot = StateFlowTurbineViewRobot(
      scheduler = dispatcher.scheduler,
      stateFlow = viewModel.state,
    )

    When("start") {
      repeat(2) {
        robot.collect()
      }
      Then("display error from empty query") {
        robot.listOfStates.last().error shouldBe AppError.Unknown
      }

      And("new search") {
        viewModel.search("tesla")
        repeat(3) {
          robot.collect()
        }
        Then("display list on UI") {
          robot.listOfStates.last().pageDisplayable shouldHaveSize 4
        }
      }
    }

    robot.close()

  }
}) {
  override suspend fun beforeSpec(spec: Spec) {
    Timber.plant(PrintlnTree())
    super.beforeSpec(spec)
  }

  override fun isolationMode(): IsolationMode {
    return IsolationMode.InstancePerTest
  }
}



