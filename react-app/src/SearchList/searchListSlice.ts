import { CaseReducer, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../store/configureStore";
import { fetchSearchList } from "./action";
import { SearchListState } from "./types";

const urlParams = new URLSearchParams(window.location.search);
const paramAll = urlParams.get("all");
const paramSids = urlParams.getAll("sid");

const initialState: SearchListState = {
    selectedSiteIds: paramSids !== null ? paramSids : [],
    selectAllToggled: paramAll !== null ? (paramAll === 'true') : paramSids === null || paramSids.length === 0,
    loading: false,
    data: [],
    errors: undefined
}

const toggleAll: CaseReducer<SearchListState, PayloadAction<void>> = (state, action) => {
    if (state.selectAllToggled) {
        return {
            ...state,
            selectedSiteIds: [],
            selectAllToggled: false
        };
    } else {
        return {
            ...state,
            selectedSiteIds: [
                ...state.data.map(el => el.siteId)
            ],
            selectAllToggled: true
        }
    }
}

const checkElement: CaseReducer<SearchListState, PayloadAction<SimpleSite>> = (state, action) => {
    const toggledSiteId: string = action.payload.siteId;
    let selectedIds = [...state.selectedSiteIds];
    if (selectedIds.includes(toggledSiteId)) {
        selectedIds = selectedIds.filter((item) => item !== toggledSiteId);
    } else {
        selectedIds.push(toggledSiteId);
    }
    return {
        ...state,
        selectedSiteIds: selectedIds,
        selectAllToggled: false
    }
}

export const searchListSlice = createSlice({
    name: "searchList",
    initialState,
    reducers: {
        toggleAll,
        checkElement
    },

    // In `extraReducers` we declare 
    // all the actions:
    extraReducers: (builder) => {
        // When a server responses with the data,
        // `fetchSearchList.fulfilled` is fired:
        builder.addCase(fetchSearchList.fulfilled,
            (state, { payload }) => {
                // We add all the new components into the state
                // and change `status` back to `idle`:
                state.data = payload;
                state.loading = false;
            });

        // When a server responses with an error:
        builder.addCase(fetchSearchList.rejected,
            (state, { payload }) => {
                // We show the error message
                // and change `status` back to `idle` again.
                if (payload) {
                    state.errors = payload.message;
                } else {
                    state.errors = 'Connection failed! No payload received.';
                }
                state.loading = false;
            });
    },
});

export default searchListSlice.reducer;

export const selectSelections = (state: RootState) => {
    return state.searchList
};