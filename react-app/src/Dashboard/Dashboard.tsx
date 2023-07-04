import React from "react";
import {Navbar, Nav, NavItem, Button} from 'react-bootstrap';
import { connect } from "react-redux";
import { Link } from 'react-router-dom';
import { ApplicationState } from "../store";

type DashboardProps = {
  selectAllToggled: boolean,
  selectedSiteIds: string[]
};

function Dashboard({ selectedSiteIds, selectAllToggled }: DashboardProps) {
  function preserveParams(path: string) {
    return (e: any) => {
      const urlParams = new URLSearchParams();
      if (selectAllToggled) {
        urlParams.append('all', 'true')
      } else {
        selectedSiteIds.forEach((element: string) => {
          urlParams.append('sid', element)
        });
      }
      return `${path}?${urlParams.toString()}`;
    }
  }

  function forceUpdateData() {
    return (e: any) => {
      fetch('/providers-information/update-data', {method: 'POST'})
          .then(r => {
            /* NOP */
          })
    }
  }

  return (
    <div>
      <Navbar bg="dark" variant="dark" expand="lg" fixed="top">
        <Navbar.Brand>Providers Information</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="mr-auto">
            <Nav.Link as={Link} to={preserveParams("/sitecards")}>Home</Nav.Link>
            <Nav.Link as={Link} to={preserveParams("/sites")}>Sites</Nav.Link>
            <Nav.Link as={Link} to={preserveParams("/jira")}>Jira</Nav.Link>
            <Nav.Link as={Link} to={preserveParams("/site-description")}>Sites Readme</Nav.Link>
            <Nav.Link as={Link} to={preserveParams("/contacts")}>Contacts</Nav.Link>
          </Nav>
          <NavItem>
            <Button variant="outline-success" onClick={forceUpdateData()}>Update data</Button>
          </NavItem>
        </Navbar.Collapse>
      </Navbar>
    </div>
  )

}

const mapStateToProps = ({ searchList }: ApplicationState) => ({
  selectAllToggled: searchList.selectAllToggled,
  selectedSiteIds: searchList.selectedSiteIds
});

export default connect(mapStateToProps)(Dashboard);