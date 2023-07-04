import { Store, createStore, applyMiddleware } from "redux";

import thunk from "redux-thunk";

import { routerMiddleware } from "connected-react-router";

import { History } from "history";

import { ApplicationState, createRootReducer } from ".";
import { createBrowserHistory } from "history";

const initialState: any = {};

export const history = createBrowserHistory({ basename: process.env.PUBLIC_URL });
export const rootReducer = createRootReducer(history);
export type RootState = ReturnType<typeof rootReducer>
export const store = configureStore(history, initialState, rootReducer);

function configureStore(
  history: History,
  initialState: ApplicationState,
  rootReducer: any
): Store<ApplicationState> {
  const store = createStore(
    rootReducer,
    initialState,
    applyMiddleware(routerMiddleware(history), thunk)
  );
  return store;
}