const searchTextReducer = (state = "", action) => {
    switch(action.type){
      case 'INPUTSEARCHTEXT':
        return action.text;

      default:
        return state;
    }
  
  };

export default searchTextReducer;