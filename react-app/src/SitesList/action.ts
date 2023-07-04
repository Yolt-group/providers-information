import { SiteListElementActionTypes } from "./types";
import { Action } from "redux";
import { ThunkAction } from "redux-thunk";
import { ApplicationState } from "../store/index";

export const fetchRequest = (): ThunkAction<void, ApplicationState, unknown, Action<string>> => async dispatch => {
  try {

    let contacts: { [key: string]: string } = {};
    const contactListResponse = await fetch("/providers-information/contacts");
    const contactList = await contactListResponse.json();
    contactList.forEach((contact: any) => {
      if(contact !== null) {
        contacts[contact.siteId] = contact.standard
      }
    });
    const sitesByCountryResponse = await fetch('/providers-information/sites-list/extended')
    const sitesByCountry = await sitesByCountryResponse.json();
    const enrichedSitesByCountry = sitesByCountry.map((el: any) => {
      return {
        ...el,
        standard: contacts[el.siteId]
      }
    });

    return dispatch({
      type: SiteListElementActionTypes.FETCH_SUCCESS,
      payload: enrichedSitesByCountry
    });
  } catch (e) {
    return dispatch({
      type: SiteListElementActionTypes.FETCH_ERROR
    });
  }
}