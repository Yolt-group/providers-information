export interface JiraComponentListElement {
    siteId: string
    providerName: string
    numberOfNotDoneIssues: string
    jiraLink: string
}

export interface JiraComponentListState {
    readonly loading: boolean
    readonly data: JiraComponentListElement[]
    readonly errors?: string
}