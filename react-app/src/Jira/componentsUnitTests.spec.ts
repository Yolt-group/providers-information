import { store } from '../store/configureStore';
import { fetchComponents } from './action';

test('Components propagate with Jira List', () => {

  let components = store.getState().components;
  expect(components.data).toHaveLength(0);
  store.dispatch({ type: fetchComponents.fulfilled.type, payload: [
    {
      numberOfNotDoneIssues: "5",
      jiraLink: "https://jira.net",
      providerName: "ING Bank Śląski",
      siteId: "fb94f208-6d34-425f-a3f8-e5b87794aef1"
    },
    {
      providerName: "PKO BP",
      siteId: "fb94f208-6d34-425f-a3f8-e5b87794aef2",
      numberOfNotDoneIssues: "5",
      jiraLink: "https://jira.net"
    }
  ]});
  components = store.getState().components;
  expect(components.data).toHaveLength(2);
});