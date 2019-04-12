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
import {SpecificationViewComponent} from './specification-view.component';
import {Specification} from '../models/specification';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-graphiql',
  template: '<div style="height: calc(100vh - 56px)" [id]="graphiql">Loading...</div>',
})

export class GraphiQLWrapperComponent extends SpecificationViewComponent implements AfterViewChecked {
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

  public graphQLFetcher(apiEndpoint?: string) {
    return (graphQLParams) => {
      return fetch(apiEndpoint ? apiEndpoint : environment.apiUrl + '/graphql', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('token')
        },
        body: JSON.stringify(graphQLParams)
      }).then(response => response.json())
        .catch(() => 'An error occurred with the network request. ' +
          `(Check the GraphQL service at '${apiEndpoint}' is running, and accepts POST requests)`);
    };
  }

  protected render() {
    if (this.isMounted()) {
      const typeDefs = this.specification.content;
      const apiEndpoint = this.specification.metadata.endpointUrl;
      ReactDOM.render(React.createElement(GraphiQL, {
        fetcher: this.graphQLFetcher(apiEndpoint),
        schema: makeExecutableSchema({typeDefs})
      }), this.getRootDomNode());
    }
  }

  ngAfterViewChecked() {
    this.render();
  }
}
