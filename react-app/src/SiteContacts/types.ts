export interface SiteContactElement {
  siteId: string
  providerName: string
  standard: string
  contact: string
}

export enum SiteContactElementActionTypes {
  FETCH_REQUEST = "@sitecontact/FETCH_REQUEST",
  FETCH_SUCCESS = "@sitecontact/FETCH_SUCCESS",
  FETCH_ERROR = "@sitecontact/FETCH_ERROR"
}

export interface SiteContactElementState {
  readonly loading: boolean;
  readonly data: SiteContactElement[];
  readonly errors?: string;
}