import {AfterViewInit, Component, OnChanges, OnDestroy, OnInit} from '@angular/core';
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import * as uuid from 'uuid';
import * as invariant from 'invariant';
import GraphiQL from 'graphiql';

@Component({
  selector: 'graphiql',
  template: '<div style="height: 100vh" [id]="graphiql">Loading...</div>',
})

export class GraphiQLWrapperComponent implements OnInit, OnDestroy, OnChanges, AfterViewInit {
  public graphiql: string;

  protected getRootDomNode() {
    const node = document.getElementById(this.graphiql);
    invariant(node, `Node '${this.graphiql} not found!`);
    return node;
  }

  private isMounted(): boolean {
    return !!this.graphiql;
  }

  private graphQLFetcher(graphQLParams) {
    return fetch(window.location.origin + '/graphql', {
      method: 'post',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(graphQLParams),
    }).then(response => response.json());
  }

  protected render() {
    if (this.isMounted()) {
      ReactDOM.render(React.createElement(GraphiQL, {fetcher: this.graphQLFetcher}), this.getRootDomNode());
    }
  }

  ngOnInit() {
    this.graphiql = uuid.v1();
  }

  ngOnChanges() {
    this.render();
  }

  ngAfterViewInit() {
    this.render();
  }

  ngOnDestroy() {
    ReactDOM.unmountComponentAtNode(this.getRootDomNode())
  }
}
