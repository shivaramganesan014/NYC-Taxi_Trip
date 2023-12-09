import React from 'react';
import './index.css';
import App from './App';
import ReactDOM from 'react-dom';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Provider } from "react-redux";
import { applyMiddleware, compose, createStore } from "redux";
import { taskMiddleware } from "react-palm/tasks";
import allReducers from "./redux/reducers";
import Visualization from './visualization/visualization';


const initialState = {};
const enhancers = [applyMiddleware(taskMiddleware)];
let store = createStore(allReducers, initialState, compose(...enhancers));

ReactDOM.render(
  <Provider store={store}>
    <Router>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/visualization" element={<Visualization />} />
      </Routes>
    </Router>
  </Provider>,

  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
