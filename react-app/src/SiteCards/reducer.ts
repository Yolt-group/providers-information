import { Reducer } from "redux";
import { SiteCardElementState, SiteCardElementActionTypes } from "./types";

export const initialState: SiteCardElementState = {
    loading: false,
    data: [],
    errors: undefined
}

const reducer: Reducer<SiteCardElementState> = (state = initialState, action) => {
    switch (action.type) {
        case SiteCardElementActionTypes.FETCH_REQUEST: {
            return { ...state, loading: true };
        }
        case SiteCardElementActionTypes.FETCH_SUCCESS: {
            return { ...state, loading: false, data: action.payload };
        }
        case SiteCardElementActionTypes.FETCH_ERROR: {
            return { ...state, loading: false, errors: action.payload };
        }
        default: {
            return state;
        }
    }
}

export { reducer as SiteCardElementreducer };