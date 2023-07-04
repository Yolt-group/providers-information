import { Reducer } from "redux";
import { SiteContactElementActionTypes, SiteContactElementState } from "./types";
export const initialState: SiteContactElementState = {
  data: [],
  errors: undefined,
  loading: false
};
const reducer: Reducer<SiteContactElementState> = (state = initialState, action) => {
  switch (action.type) {
    case SiteContactElementActionTypes.FETCH_REQUEST: {
      return { ...state, loading: true };
    }
    case SiteContactElementActionTypes.FETCH_SUCCESS: {
      return { ...state, loading: false, data: action.payload };
    }
    case SiteContactElementActionTypes.FETCH_ERROR: {
      return { ...state, loading: false, errors: action.payload };
    }
    default: {
      return state;
    }
  }
};
export { reducer as SiteContactElementReducer };