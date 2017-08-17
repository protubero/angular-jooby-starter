import {Component, OnInit, Input, OnChanges} from '@angular/core';
import {Validators, FormControl, FormGroup, FormBuilder} from '@angular/forms';
import {Location} from '@angular/common';
import {Message} from 'primeng/components/common/api';
import {ActivatedRoute, Router, ParamMap, UrlSegment} from '@angular/router';
import {PersonsService} from './persons.service';
import {Person} from '../model';
import 'rxjs/add/operator/switchMap';
import {Observable} from 'rxjs/Observable';

const enum Mode {
  Edit,
  Create
}

@Component({
  selector: 'app-person-editor',
  templateUrl: './person-editor.component.html'
})
export class PersonEditorComponent implements OnInit, OnChanges {

  mode: Mode;
  title: string;
  @Input() person: Person;
  msgs: Message[] = [];
  fbgroup: FormGroup;
  submitted: boolean;

  constructor(private fb: FormBuilder, private route: ActivatedRoute,
    private router: Router, private location: Location,
  private ps: PersonsService) {}

  ngOnInit() {
    this.fbgroup = this.fb.group({
      'name': new FormControl('', [Validators.required, Validators.minLength(3)]),
      'mail': new FormControl('', [Validators.required]),
      'age': new FormControl('', [Validators.required, Validators.min(18), Validators.max(99)])
    });

    this.route.url.subscribe(u => {
      console.log(u[u.length - 1].path);
      if (u[u.length - 1].path === 'new') {
        this.mode = Mode.Create;
        this.title = 'Create new person';
      } else {
        this.mode = Mode.Edit;
        this.title = 'Edit person';
        this.route.paramMap
          .switchMap((params: ParamMap) =>
            this.ps.findById(+params.get('id')))
          .subscribe((person: Person) => {
            this.person = person;
            // why doesn't that happen automatically?
            this.ngOnChanges();
          });
      }
    });
  }

  ngOnChanges() {
    this.fbgroup.reset({
      name: this.person.name,
      mail: this.person.email,
      age: this.person.age
    });
  }

  onSubmit() {
    this.person = this.prepareSave();
    let req: Observable<any>;

    switch (this.mode) {
      case Mode.Edit: {
        req = this.ps.update(this.person.id, this.person);
        break;
      }
      case Mode.Create: {
        req = this.ps.submit(this.person);
        break;
      }
    }
    req.subscribe(p => {
      this.location.back();
    });

    // onError
    // this.msgs = [];
    // this.msgs.push({severity: 'info', summary: 'Success', detail: 'Person saved: ' + p.name});
    // this.ngOnChanges();
  }

  onCancel() {
    this.location.back();
  }

  prepareSave(): Person {
    const formModel = this.fbgroup.value;

    // return new `Person` object containing a combination of original person value(s)
    // and copies of changed form model values
    const savePerson: Person = {
      id: null,
      name: formModel.name as string,
      email: formModel.mail as string,
      age: formModel.age as number
    };
    if (this.mode === Mode.Edit) {
      savePerson.id = this.person.id;
    }
    return savePerson;
  }

  get diagnostic() {return JSON.stringify(this.fbgroup.value); }

}

