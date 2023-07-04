import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "../store/configureStore";
import { fetchComponents } from "./action";
import { JiraComponentListState } from "./types";


const initialState: JiraComponentListState = {
    loading: false,
    data: [],
    errors: undefined
}

export const componenetsSlice = createSlice({
    name: "components",
    initialState,
    reducers: {
        // ...
    },

    // In `extraReducers` we declare 
    // all the actions:
    extraReducers: (builder) => {
        // When a server responses with the data,
        // `fetchComponents.fulfilled` is fired:
        builder.addCase(fetchComponents.fulfilled,
            (state, { payload }) => {
                // We add all the new components into the state
                // and change `status` back to `idle`:
                state.data = payload;
                state.loading = false;
            });

        // When a server responses with an error:
        builder.addCase(fetchComponents.rejected,
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

export default componenetsSlice.reducer

export const selectComponents = (state: RootState) => {
    return state.components
};