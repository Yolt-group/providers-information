export interface SiteCardElement {
  siteId: string
  providerName: string
  jiraLink: string
  email: string
}

export enum SiteCardElementActionTypes {
  FETCH_REQUEST = "@@sitecardelement/FETCH_REQUEST",
  FETCH_SUCCESS = "@@sitecardelement/FETCH_SUCCESS",
  FETCH_ERROR = "@@sitecardelement/FETCH_ERROR"
}

export interface SiteCardElementState {
  readonly loading: boolean;
  readonly data: SiteCardElement[];
  readonly errors?: string;
}