import { SiteContactElementActionTypes } from "./types";
import { Action } from "redux";
import { ThunkAction } from "redux-thunk";
import { ApplicationState } from "../store/index";

export const fetchRequest = (): ThunkAction<void, ApplicationState, unknown, Action<string>> => async dispatch => {
  try {
    const sitesByCountryResponse = await fetch('/providers-information/sites-list')
    const sitesByCountry = await sitesByCountryResponse.json();
    let sitesDict: { [key: string]: string } = {};
    sitesByCountry.forEach((x: any) => sitesDict[x.siteId] = x.providerName);

    const contactsResponse = await fetch('/providers-information/contacts')
    const contacts = await contactsResponse.json();
    return dispatch({
      type: SiteContactElementActionTypes.FETCH_SUCCESS,
      payload: contacts.filter((el: any) => el !== null).map((el: any) => {
        return {
          ...el,
          providerName: sitesDict[el.siteId]
        }
      })
    });
  } catch (e) {
    return dispatch({
      type: SiteContactElementActionTypes.FETCH_ERROR
    });
  }
};