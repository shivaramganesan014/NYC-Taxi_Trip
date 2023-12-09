import "./visualization.css";
import React from "react";
import { KeplerGl } from 'kepler.gl';
import { Provider } from "react-redux";
import { applyMiddleware, compose, createStore } from "redux";
import { taskMiddleware } from "react-palm/tasks";
import allReducers from "../redux/reducers";
// Using kepler.gl library for the map loadup
// most functions independently handled by kepler.gl, just we need to provided up-to-date data with correct values
function Visualization() {
  const initialState = {};
const enhancers = [applyMiddleware(taskMiddleware)];
let store = createStore(allReducers, initialState, compose(...enhancers));
  // get the vizualization data form redux
  // you would have to create a account in kepler.gl website to get the mapboxApiAccessToken
  // you can use the free account if just playing around with library or building a small project
  return (
    <div>
        <Provider store={store}>
          <KeplerGl
             id="naturaldisaster"
             store={store}
             mapboxApiAccessToken={
              "pk.eyJ1IjoiYXRpc2hheWphaW4iLCJhIjoiY2xheWJiZHpqMHhwdjN2bXZzNzQ5NThtbiJ9.J2G94ZEBgJxIMmkxmLIHYw"
            }
            width={window.innerWidth}
            height={window.innerHeight} />
</Provider>
    </div>
  );
}

export default Visualization;
