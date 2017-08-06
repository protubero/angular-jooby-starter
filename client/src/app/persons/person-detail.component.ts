import {Component, OnInit, Input, OnChanges} from '@angular/core';
import {ActivatedRoute, Router, ParamMap, UrlSegment} from '@angular/router';
import {Person} from '../model';
import {PersonsService} from './persons.service';


@Component({
  selector: 'app-person-detail',
  templateUrl: './person-detail.component.html'
})
export class PersonDetailComponent implements OnInit {

  @Input() person: Person;
  isRouted: boolean;
  @Input() showTitle = false;

  constructor(private route: ActivatedRoute,
    private router: Router, private ps: PersonsService) {}

  ngOnInit() {
    this.isRouted = !this.person;
    if (this.isRouted) {
      this.showTitle = true;

      this.route.url.subscribe(u => {
          this.route.paramMap
            .switchMap((params: ParamMap) =>
              this.ps.findById(+params.get('id')))
            .subscribe((person: Person) => {
              this.person = person;
            });
      });
    }
  }

}

