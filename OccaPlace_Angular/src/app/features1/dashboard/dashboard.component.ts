import {Component, OnInit, ViewChild} from '@angular/core';
import { MatSidenav } from "@angular/material/sidenav";
import { BreakpointObserver, Breakpoints } from "@angular/cdk/layout";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isMobile = false;

  constructor(private breakpointObserver: BreakpointObserver) {}

  ngOnInit() {
    this.breakpointObserver.observe([Breakpoints.Handset])
      .subscribe(result => {
        this.isMobile = result.matches;
        if (this.isMobile) {
          this.sidenav.close();
        } else {
          this.sidenav.open();
        }
      });
  }
  sidebarOpen = true;

  onSidebarToggle(isOpen: boolean) {
    this.sidebarOpen = isOpen;
  }

}
