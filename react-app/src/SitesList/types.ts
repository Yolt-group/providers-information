export interface SiteListElement {
  siteId: string
  providerName: string
  selectedSite: boolean
  services: string[]
  availableInCountries: string[]
  supportedAccounts: string
  standard: string
}

export enum SiteListElementActionTypes {
  FETCH_REQUEST = "@@sitelist/FETCH_REQUEST",
  FETCH_SUCCESS = "@@sitelist/FETCH_SUCCESS",
  FETCH_ERROR = "@@sitelist/FETCH_ERROR"
}

export interface SiteListElementState {
  readonly loading: boolean;
  readonly data: SiteListElement[];
  readonly errors?: string;
}