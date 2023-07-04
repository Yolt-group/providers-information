import { createAsyncThunk } from "@reduxjs/toolkit";

export const fetchSearchList = createAsyncThunk<
  any[],
  number,
  { rejectValue: any }
>(
  "searchList/fetch",
  // The second argument, `thunkApi`, is an object
  // that contains all those fields
  // and the `rejectWithValue` function:
  async (_: number, thunkApi) => {
    const response = await fetch('/providers-information/sites-list');

    // Check if status is not okay:
    if (response.status !== 200) {
      // Return the error message:
      return thunkApi.rejectWithValue({
        message: "Failed to fetch sites list."
      });
    }
    const data: any = await response.json();
    return data;
  }
);
