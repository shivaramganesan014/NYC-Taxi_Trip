const apiResultReducer = (state = { state: "" }, action) => {
    switch (action.type) {
      case "APIRESULT":
        return action.text;
  
      default:
        return state;
    }
  };
  
  export default apiResultReducer;
  