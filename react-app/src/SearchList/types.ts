export interface SearchListElement {
    siteId: string
    providerName: string
    providerKey: string
    selectedSite: boolean
}

export interface SearchListState {
    readonly selectAllToggled: boolean
    readonly selectedSiteIds: string[]
    readonly loading: boolean
    readonly data: SearchListElement[]
    readonly errors?: string
}