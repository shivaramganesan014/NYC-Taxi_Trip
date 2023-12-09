import counterReducer from "./counter";
import searchTextReducer from "./searchText";
import searchModelDetailsReducer from "./searchModelDetails";
import keplerGlReducer from "kepler.gl/reducers";
import visualModelReducer from "./visualData";
import apiResultReducer from "./apiResult";
import { combineReducers } from "redux";

const allReducers = combineReducers({
  counter: counterReducer,
  searchText: searchTextReducer,
  searchModelDetails: searchModelDetailsReducer,
  keplerGl: keplerGlReducer,
  visualData: visualModelReducer,
  apiResult: apiResultReducer
});

export default allReducers;
