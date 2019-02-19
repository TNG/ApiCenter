import {AfterViewInit, Component, OnChanges, OnDestroy, OnInit} from '@angular/core';
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import * as uuid from 'uuid';
import * as invariant from 'invariant';
import GraphiQL from 'graphiql';
import fetch from 'isomorphic-fetch';
import {environment} from '../../environments/environment';
import {GraphQLSchema} from 'graphql';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {Version} from '../models/version';
import {makeExecutableSchema} from 'graphql-tools';
import {typeDefs} from './schema';

@Component({
  selector: 'app-graphiql',
  template: '<div style="height: calc(100vh - 56px)" [id]="graphiql">Loading...</div>',
})

export class GraphiQLWrapperComponent implements OnInit, OnDestroy, OnChanges, AfterViewInit {
  public graphiql: string;
  private specification: Version;

  constructor(private route: ActivatedRoute, private http: HttpClient) {
  }

  protected getRootDomNode() {
    const node = document.getElementById(this.graphiql);
    invariant(node, `Node '${this.graphiql} not found!`);
    return node;
  }

  private isMounted(): boolean {
    return !!this.graphiql;
  }

  public graphQLFetcher(graphQLParams) {
    return fetch('http://localhost:4000/graphql', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(graphQLParams)
    }).then(response => response.json());
  }

  protected render() {
    if (this.isMounted()) {
      ReactDOM.render(React.createElement(GraphiQL, {
        fetcher: this.graphQLFetcher,
        schema: makeExecutableSchema({typeDefs})
      }), this.getRootDomNode());
    }
  }

  ngOnInit() {
    this.graphiql = uuid.v1();
    this.route.params.subscribe(params => {
      this.http.get<Version>(environment.apiUrl + '/specifications/' + params['specificationId'] + '/versions/' + params['version'])
        .subscribe(data => {
          this.specification = data;
          console.log(data);
        });
    });
  }

  ngOnChanges() {
    this.render();
  }

  ngAfterViewInit() {
    this.render();
  }

  ngOnDestroy() {
    ReactDOM.unmountComponentAtNode(this.getRootDomNode());
  }
}
