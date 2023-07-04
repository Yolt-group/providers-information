import { combineReducers } from "redux";
import { connectRouter } from "connected-react-router";

import { History } from "history";

import { SiteListElementReducer } from "../SitesList/reducer";
import { SiteListElementState } from "../SitesList/types"
import { SearchListState } from "../SearchList/types";
import { JiraComponentListState } from "../Jira/types";
import { SiteContactElementState } from "../SiteContacts/types";
import { SiteContactElementReducer } from "../SiteContacts/reducer";
import { SiteCardElementState } from "../SiteCards/types";
import { SiteCardElementreducer } from "../SiteCards/reducer";
import { SiteReadmeListElementState } from "../SiteReadmeList/types";
import componentsReducer from "../Jira/componentsSlice"
import searchListReducer from "../SearchList/searchListSlice"
import siteReadmeReducer from "../SiteReadmeList/siteReadmeSlice"


export interface ApplicationState {
  siteList: SiteListElementState
  searchList: SearchListState
  siteContactElement: SiteContactElementState
  siteCardElement: SiteCardElementState
  siteReadmeElement: SiteReadmeListElementState
  components: JiraComponentListState
}

export const createRootReducer = (history: History) =>
  combineReducers({
    siteList: SiteListElementReducer,
    searchList: searchListReducer,
    siteContactElement: SiteContactElementReducer,
    siteCardElement: SiteCardElementreducer,
    siteReadmeElement: siteReadmeReducer,
    components: componentsReducer,
    router: connectRouter(history)
  });
