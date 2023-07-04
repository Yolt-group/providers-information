import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'github-markdown-css';

import MainPage from './MainPage';
import { Provider } from "react-redux"
import {store, history} from "./store/configureStore";

ReactDOM.render(
  <Provider store={store}>
    <React.StrictMode>
      <MainPage store={store} history={history} />
    </React.StrictMode>
  </Provider>,

  document.getElementById('root')
);