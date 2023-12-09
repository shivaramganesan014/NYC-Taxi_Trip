// Actions
export const increment = () => {
  return {
    type: "INCREMENT",
  };
};

export const decrement = () => {
  return {
    type: "DECREMENT",
  };
};

export const searchText = (input) => {
  return {
    type: "INPUTSEARCHTEXT",
    text: input
  };
}

export const searchModelDetails = (input) => {
  return {
    type: "SEARCHMODELDETAILS",
    text: input
  };
}

export const visualData = (input) => {
  return {
    type: "VISUALMODEL",
    text: input
  };
};

export const apiResult = (input) => {
  return {
    type: "APIRESULT",
    text: input
  };
};

