import {
  AfterViewChecked,
  Component,
  Input,
  OnDestroy,
} from '@angular/core';
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import * as uuid from 'uuid';
import * as invariant from 'invariant';
import GraphiQL from 'graphiql';
import fetch from 'isomorphic-fetch';
import {GraphQLSchema} from 'graphql';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {makeExecutableSchema} from 'graphql-tools';
import {VersionViewComponent} from './version-view.component';
import {Version} from '../models/version';

@Component({
  selector: 'app-graphiql',
  template: '<div style="height: calc(100vh - 56px)" [id]="graphiql">Loading...</div>',
})

export class GraphiQLWrapperComponent extends VersionViewComponent implements OnDestroy, AfterViewChecked {
  public graphiql: string;

  constructor(route: ActivatedRoute, http: HttpClient) {
    super(route, http);
    this.graphiql = uuid.v1();
  }

  protected getRootDomNode() {
    const node = document.getElementById(this.graphiql);
    invariant(node, `Node '${this.graphiql} not found!`);
    return node;
  }

  private isMounted(): boolean {
    return !!this.graphiql && !!this.specification;
  }

  public graphQLFetcher(graphQLParams) {
    return fetch('http://localhost:4000/graphql', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(graphQLParams)
    }).then(response => response.json())
      .catch(() => 'An error occurred with the network request. ' +
        '(Check the GraphQL service is running, and accepts POST requests)');
  }

  protected render() {
    if (this.isMounted()) {
      const typeDefs = this.specification.content;
      ReactDOM.render(React.createElement(GraphiQL, {
        fetcher: this.graphQLFetcher,
        schema: makeExecutableSchema({typeDefs})
      }), this.getRootDomNode());
    }
  }

  ngAfterViewChecked() {
    this.render();
  }

  ngOnDestroy() {
    ReactDOM.unmountComponentAtNode(this.getRootDomNode());
  }
}
