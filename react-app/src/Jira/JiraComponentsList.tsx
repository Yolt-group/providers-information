import React, { useEffect } from "react";
import { Button } from 'react-bootstrap'
import Table from 'react-bootstrap/Table'
import { useDispatch, useSelector } from "react-redux";
import { fetchComponents } from "./action";
import { JiraComponentListState } from "./types";
import { selectComponents } from "./componentsSlice";
import { selectSelections } from "../SearchList/searchListSlice";

export const JiraComponentsList = () => {
    const dispatch = useDispatch()
    const state: JiraComponentListState = useSelector(selectComponents)
    const {selectedSiteIds, selectAllToggled} = useSelector(selectSelections)
  
    useEffect(() => {
      dispatch(fetchComponents(0))
    }, [dispatch])

    const filteredSitesByCountry = state.data
    .filter((el: any) => {
        return selectAllToggled || selectedSiteIds.includes(el.siteId);
    })
    .map((el: any) => (
        <tr>
            <td>{el.providerName}</td>
            <td>{el.siteId}</td>
            <td>{el.numberOfNotDoneIssues}</td>
            <td><Button href={el.jiraLink} target="_blank">Link</Button></td>
        </tr>
    ));
    return (
        <Table striped hover size="sm">
            <thead>
                <tr>
                    <th>Provider Name</th>
                    <th>Site ID</th>
                    <th>Number Of Not Done Issues</th>
                    <th>Jira Link</th>
                </tr>
            </thead>
            <tbody>
                {filteredSitesByCountry}
            </tbody>
        </Table>
    );
}