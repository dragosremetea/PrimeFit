import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Diet, DietCategory } from 'src/app/models/diet.model';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { DietService } from 'src/app/services/diet.service';
import { SidenavComponent } from '../sidenav/sidenav.component';

@Component({
  selector: 'app-add-diet',
  templateUrl: './add-diet.component.html',
  styleUrls: ['./add-diet.component.css']
})
export class AddDietComponent implements OnInit {

  category: DietCategory[] = [
    DietCategory.LOW_CARB,
    DietCategory.VEGAN,
    DietCategory.GLUTEN_FREE,
    DietCategory.PALEO,
    DietCategory.MEDITERRANEAN,
  ];

  selectedFile: any = null;

  userObject!: User;

  diet: Diet = new Diet();

  dataSource: Diet[] = [];

  id: any;

  displayedColumns: string[] = ['Name', 'Category', 'Actions'];

  DietCategory = DietCategory;

  constructor(
    public dietService: DietService,
    public router: Router,
    public authService: AuthService,
    public sidenav: SidenavComponent
  ) { }

  ngOnInit(): void {
    this.getAllDiets();
  }

  onFileSelected(event: any): void {
      this.selectedFile = event.target.files[0] ?? null;
  }

  saveDiet() {
    this.dietService.createDiet(this.diet, this.selectedFile).subscribe(data => {
      this.router.navigate(['/dashboard']);
      this.refresh();
      this.sidenav.pressDiets();
      this.diet.name = "";
      this.diet.pdfUrl = "";
      this.selectedFile = "";
    })
  }

  deleteDiet(row: any) {
    this.dietService.delete(row.id).subscribe(data => {
      this.refresh();
    })
  }

  sendEmail(row: any) {
    this.id = localStorage.getItem('currentUser')
    this.dietService.send(row.id, parseInt(this.id)).subscribe(data => {
    });
  }

  getAllDiets() {
    this.dietService.getDiets().subscribe(data => {
      this.dataSource = data;
    })
  }

  refresh(): void {
    this.getAllDiets();
  }

  getDietCategoryAsString(categories: DietCategory[]): string {
    let categoryAsString = "";
    categories.filter((x: DietCategory) => {
      if(x == 0) {
        categoryAsString = 'LOW_CARB';
      }
      if(x == 1) {
        categoryAsString = 'VEGAN';
      }
      if(x == 2) {
        categoryAsString = 'GLUTEN_FREE';
      }
      if(x == 3) {
        categoryAsString = 'PALEO';
      }
      if(x == 4) {
        categoryAsString = 'MEDITERRANEAN';
      }
    });
    return categoryAsString;
  }
}
