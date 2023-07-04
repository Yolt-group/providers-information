import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "../store/configureStore";
import { fetchReadme } from "./action";
import { SiteReadmeListElementState } from "./types";

export const initialState: SiteReadmeListElementState = {
    data: [],
    errors: undefined,
    loading: false
};

export const siteReadmeSlice = createSlice({
    name: "siteReadme",
    initialState,
    reducers: {},

    // In `extraReducers` we declare 
    // all the actions:
    extraReducers: (builder) => {
        // When a server responses with the data,
        // `fetchSearchList.fulfilled` is fired:
        builder.addCase(fetchReadme.fulfilled,
            (state, { payload }) => {
                // We add all the new components into the state
                // and change `status` back to `idle`:
                state.data = payload;
                state.loading = false;
            });

        // When a server responses with an error:
        builder.addCase(fetchReadme.rejected,
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

export default siteReadmeSlice.reducer;

export const selectReadme = (state: RootState) => {
    return state.siteReadmeElement
};