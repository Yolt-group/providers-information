import { Reducer } from "redux";
import { SiteListElementActionTypes, SiteListElementState } from "./types";
export const initialState: SiteListElementState = {
  data: [],
  errors: undefined,
  loading: false
};
const reducer: Reducer<SiteListElementState> = (state = initialState, action) => {
  switch (action.type) {
    case SiteListElementActionTypes.FETCH_REQUEST: {
      return { ...state, loading: true };
    }
    case SiteListElementActionTypes.FETCH_SUCCESS: {
      return { ...state, loading: false, data: action.payload };
    }
    case SiteListElementActionTypes.FETCH_ERROR: {
      return { ...state, loading: false, errors: action.payload };
    }
    default: {
      return state;
    }
  }
};
export { reducer as SiteListElementReducer };