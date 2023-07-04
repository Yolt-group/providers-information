import React, { useEffect } from "react"
import { Button, Card, ListGroup } from 'react-bootstrap'
import { ApplicationState } from "../store"
import { connect } from "react-redux"
import { ThunkDispatch } from "redux-thunk"
import { AnyAction } from "redux"
import { fetchRequest } from "./action"
import { SiteCardElement } from "./types"

type SiteCardsListProps = {
    selectAllToggled: boolean,
    selectedSiteIds: string[],
    cards: SiteCardElement[]
}

interface propsFromDispatch {
    fetchRequest: () => any
}

function SiteCards({ selectAllToggled, selectedSiteIds, cards, fetchRequest }: SiteCardsListProps & propsFromDispatch) {
    useEffect(() => {
        fetchRequest();
    }, [fetchRequest]);

    const filteredSitesDetailed = cards.filter((el: any) => {
        return selectAllToggled || selectedSiteIds.includes(el.siteId);
    }).map((el: any) => (
        <div className="col-sm-4 mt-1">
            <Card>
                <Card.Img src={el.iconUrl} style={{opacity: 0.05}}/>
                <Card.ImgOverlay style={{padding:0}}>
                    <CardHeader maintenanceStatus={el.maintenanceStatus} providerName={el.providerName}/>
                        <Card.Subtitle>Site id: {el.siteId}</Card.Subtitle>
                        <Card.Subtitle>Provider identifier: {el.providerKey}</Card.Subtitle>
                        <Card.Subtitle>Repository name: {el.repositoryName}</Card.Subtitle>
                    <hr />
                    <ListGroup>
                        <JiraLink jiraLink={el.jiraLink}/>
                        <RepositoryLink repositoryLink={el.repositoryLink}/>
                    </ListGroup>
                    <Card.Body>
                        <Button href={`site-description?sid=${el.siteId}`} variant="primary"
                                target="_blank">ReadMe</Button>
                        <Button disabled={isSendMailDisabled(el.email)} href={`mailto:` + el.email} variant="primary"
                                style={{float: 'right'}}>Send e-mail</Button>
                    </Card.Body>
                </Card.ImgOverlay>
                <CardFooter maintenanceStatus={el.maintenanceStatus} maintenanceFrom={el.maintenanceFrom}
                            maintenanceTo={el.maintenanceTo}/>
            </Card>
        </div>
    ));
    return (<div className="row">{filteredSitesDetailed}</div>);
}

function isSendMailDisabled(email: string) {
    return email === null || email === undefined;
}

const mapStateToProps = ({ searchList, siteCardElement }: ApplicationState) => ({
    data: searchList.data,
    selectAllToggled: searchList.selectAllToggled,
    selectedSiteIds: searchList.selectedSiteIds,
    cards: siteCardElement.data
});

const mapDispatchToProps = (dispatch: ThunkDispatch<any, any, AnyAction>) => {
    return {
        fetchRequest: () => {
            dispatch(fetchRequest());
        }
    };
};

interface CardHeaderProps {
    maintenanceStatus: string
    providerName: string
}

const CardHeader = ({maintenanceStatus, providerName}: CardHeaderProps) => {
    if(maintenanceStatus === 'SCHEDULED') {
        return <Card.Header style={{ background: '#ffc107' }}>{providerName}</Card.Header>
    } else if(maintenanceStatus === 'MAINTENANCE') {
        return <Card.Header style={{ background: '#dc3545' }}>{providerName}</Card.Header>
    } else {
        return <Card.Header>{providerName}</Card.Header>
    }
}

interface CardFooterProps {
    maintenanceStatus: string
    maintenanceFrom: string
    maintenanceTo: string
}

const CardFooter = ({maintenanceStatus, maintenanceFrom, maintenanceTo}: CardFooterProps) => {
    if(maintenanceStatus === 'SCHEDULED' || maintenanceStatus === 'MAINTENANCE') {
        return (
            <Card.Footer>
                <ListGroup>
                    <small className="text-muted">Maintenance status: {maintenanceStatus}</small>
                    <small className="text-muted">From: {maintenanceFrom}</small>
                    <small className="text-muted">To: {maintenanceTo}</small>
                </ListGroup>
            </Card.Footer>
        )
    }

    return <Card.Footer>
        <small className="text-muted">Maintenance status: {maintenanceStatus}</small>
    </Card.Footer>;
}

const JiraLink = ({jiraLink}: any) => {
    if (jiraLink === null || jiraLink === undefined) {
        return <p className="empty-link">Jira (no open tickets)</p>
    }

    return <a className="card-link" href={jiraLink} target="_blank" rel="noopener noreferrer">Jira</a>
}

const RepositoryLink = ({repositoryLink}: any) => {
    return <a className="card-link" href={repositoryLink} target="_blank" rel="noopener noreferrer">Repository</a>
}

export default connect(mapStateToProps, mapDispatchToProps)(SiteCards);