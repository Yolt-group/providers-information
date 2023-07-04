import { Action } from "redux";
import { ThunkAction } from "redux-thunk";
import { ApplicationState } from "../store/index";
import { SiteCardElementActionTypes } from "./types";

export const fetchRequest = (): ThunkAction<void, ApplicationState, unknown, Action<string>> => async dispatch => {
  try {
    const sitesDetailedPromise = await fetch("/providers-information/sites-list/extended");
    const sitesDetailed = await sitesDetailedPromise.json();
    let contactDict: { [key: string]: string } = {};
    const contactListPromise = await fetch("/providers-information/contacts");
    const contactList = await contactListPromise.json();
    contactList.forEach((contact: any) => {
      if(contact !== null) {
        contactDict[contact.siteId] = contact.contact
      }
    });

    let jiraDict: { [key: string]: string } = {};
    const jiraListPromise = await fetch("/providers-information/components-list");
    const jiraList = await jiraListPromise.json();
    jiraList.forEach((jira: any) => jiraDict[jira.siteId] = jira.jiraLink);

    const repositoryListPromise = await fetch("/providers-information/repositories");
    const repositoryList = await repositoryListPromise.json();
    let repositoryNameDict: { [key: string]: string } = {};
    let repositoryLinkDict: { [key: string]: string } = {};
    repositoryList.forEach((repository: any) => {
      if(repository !== null) {
        repositoryNameDict[repository.siteId] = repository.name;
        repositoryLinkDict[repository.siteId] = repository.link;
      }
    });

    return dispatch({
      type: SiteCardElementActionTypes.FETCH_SUCCESS,
      payload: sitesDetailed.map((el: any) => {
        return {
          ...el,
          email: contactDict[el.siteId],
          jiraLink: jiraDict[el.siteId],
          maintenanceStatus: el.maintenanceStatus,
          maintenanceFrom: el.maintenanceFrom,
          maintenanceTo: el.maintenanceTo,
          iconUrl: el.iconUrl,
          repositoryName: repositoryNameDict[el.siteId],
          repositoryLink: repositoryLinkDict[el.siteId],
        }
      })
    });
  } catch (e) {
    return dispatch({
      type: SiteCardElementActionTypes.FETCH_ERROR
    });
  }
};