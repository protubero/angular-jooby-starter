import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Message} from 'primeng/components/common/api';
import {ConfirmDialogModule, ConfirmationService} from 'primeng/primeng';
import {Person, PersonsService} from './persons.service';

@Component({
  selector: 'app-persons',
  templateUrl: './persons.component.html'
})
export class PersonsComponent implements OnInit {

  persons: Person[];
  msgs: Message[] = [];

  constructor(private http: HttpClient, private confirmationService: ConfirmationService, private router: Router,
     private ps: PersonsService) {}

  ngOnInit() {
    this.ps.list().subscribe(persons => {
      this.persons = persons;
    });
  }

  addPerson() {
      this.router.navigate(['persons/new']);
  }

  editPerson(person: Person) {
      this.router.navigate(['persons', person.id]);
  }

  deletePerson(person: Person) {
    this.confirmationService.confirm({
            message: 'Are you sure that you want to delete person [' + person.name + '] ?',
            accept: () => {
                // Actual logic to perform a confirmation
                this.ps.remove(person.id).subscribe(r => {
                    this.msgs = [];
                    this.msgs.push({severity: 'info', summary: 'Success', detail: 'Person deleted: ' + person.name});
                    this.ngOnInit();
                });
            }
        });
  }

}


