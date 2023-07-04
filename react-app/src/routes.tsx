import React from "react";
import { Route, Switch } from "react-router-dom";
import Dashboard from "./Dashboard/Dashboard";
import { JiraComponentsList } from "./Jira/JiraComponentsList";
import { SearchList } from "./SearchList/SearchList"
import SiteCards from "./SiteCards/SiteCards";
import SiteContacts from "./SiteContacts/SiteContacts";
import { SiteReadmeList } from "./SiteReadmeList/SiteReadmeList";
import SitesList from "./SitesList/SitesList";

const Routes: React.SFC = () => (
    <div>
        <Dashboard />
        <Switch>
            <Route path="/sites">
                <div className="container-fluid">
                    <div className="row">
                        <SearchList />
                        <div className="col-sm-10 mt-3 rightpanel ml-auto">
                            <div className="row">
                                <div className="container-fluid">
                                    <div className="col-sm-12">
                                        <SitesList />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Route>
            <Route path="/jira">
                <div className="container-fluid">
                    <div className="row">
                        <SearchList />
                        <div className="col-sm-10 mt-3 rightpanel ml-auto">
                            <div className="row">
                                <div className="container-fluid">
                                    <div className="col-sm-12">
                                        <JiraComponentsList />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Route>
            <Route path="/site-description">
                <div className="container-fluid">
                    <div className="row">
                        <SearchList />
                        <div className="col-sm-10 mt-3 rightpanel ml-auto">
                            <div className="row">
                                <div className="container-fluid">
                                    <div className="col-sm-12">
                                        <SiteReadmeList />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Route>
            <Route path="/contacts">
                <div className="container-fluid">
                    <div className="row">
                        <SearchList />
                        <div className="col-sm-10 mt-3 rightpanel ml-auto">
                            <div className="row">
                                <div className="container-fluid">
                                    <div className="col-sm-12">
                                        <SiteContacts />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Route>
            <Route path={["/", "/sitecards"]}>
                <div className="container-fluid">
                    <div className="row">
                        <SearchList />
                        <div className="col-sm-10 mt-3 rightpanel ml-auto">
                            <SiteCards />
                        </div>
                    </div>
                </div>
            </Route>
        </Switch>
    </div>
);

export default Routes;