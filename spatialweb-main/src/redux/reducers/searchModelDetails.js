const searchModelDetailsReducer = (state = { id: "" }, action) => {
  switch (action.type) {
    case "SEARCHMODELDETAILS":
      return action.text;

    default:
      return state;
  }
};

export default searchModelDetailsReducer;
