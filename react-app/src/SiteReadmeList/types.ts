export interface SiteReadmeListElement {
  siteId: string
  content: string
}

export interface SiteReadmeListElementState {
  readonly loading: boolean;
  readonly data: SiteReadmeListElement[];
  readonly errors?: string;
}