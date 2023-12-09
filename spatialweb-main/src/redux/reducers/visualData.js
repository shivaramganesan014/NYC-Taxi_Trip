const visualModelReducer = (state = { id: "" }, action) => {
    switch (action.type) {
      case "VISUALMODEL":
        return action.text;
  
      default:
        return state;
    }
  };
  
  export default visualModelReducer;
  