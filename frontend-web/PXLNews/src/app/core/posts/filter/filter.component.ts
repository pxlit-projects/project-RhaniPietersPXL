import {Component, EventEmitter, Output} from '@angular/core';
import {Filter} from "../../../shared/models/filter.model";
import {FormsModule} from "@angular/forms";

@Component({
    selector: 'app-filter',
    standalone: true,
    imports: [FormsModule],
    templateUrl: './filter.component.html',
    styleUrl: './filter.component.css'
})
export class FilterComponent {
    filter: Filter = {content: '', category: '', author: ''};

    @Output() filterChanged: EventEmitter<Filter> = new EventEmitter<Filter>();

    onSubmit(form: any): void {
        if (form.valid) {
            this.filter.content = this.filter.content.toLowerCase();
            this.filter.category = this.filter.category.toLowerCase();
            this.filter.author = this.filter.author.toLowerCase();
            this.filterChanged.emit(this.filter);
        }
    }
}
