import React from "react";
import "./App.css";
import { ApplicationState } from "./store";
import { Store } from "redux";
import { History } from "history";
import { ConnectedRouter } from "connected-react-router";
import { Provider } from "react-redux";
import Routes from "./routes";

interface MainProps {
  store: Store<ApplicationState>;
  history: History;
}

function MainPage({ store, history }: MainProps) {
  return (
    <Provider store={store}>
      <ConnectedRouter history={history}>
        <Routes />
      </ConnectedRouter>
    </Provider>
  );
}

export default MainPage;