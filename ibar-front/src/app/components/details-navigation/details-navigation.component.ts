import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-details-navigation',
  templateUrl: './details-navigation.component.html',
  styleUrls: ['./details-navigation.component.scss']
})
export class DetailsNavigationComponent {

  @Input()
  navigationItems: {label: string, link: string, icon?: string}[] = [];

}
