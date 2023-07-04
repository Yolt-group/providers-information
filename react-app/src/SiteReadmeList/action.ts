import { createAsyncThunk } from "@reduxjs/toolkit";

export const fetchReadme = createAsyncThunk<
  any[],
  number,
  { rejectValue: any }
>(
  "readme/fetch",
  // The second argument, `thunkApi`, is an object
  // that contains all those fields
  // and the `rejectWithValue` function:
  async (limit: number, thunkApi) => {
    const response = await fetch('/providers-information/readme');

    // Check if status is not okay:
    if (response.status !== 200) {
      // Return the error message:
      return thunkApi.rejectWithValue({
        message: "Failed to fetch readme."
      });
    }
    const data: any = await response.json();
    return data;
  }
);


