import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Diet, DietCategory } from 'src/app/models/diet.model';
import { AuthService } from 'src/app/services/auth.service';
import { DietService } from 'src/app/services/diet.service';

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

  diet: Diet = new Diet();

  dataSource: Diet[] = [];

  displayedColumns: string[] = ['Name', 'Category', 'Actions'];

  DietCategory = DietCategory;

  constructor(
    public dietService: DietService,
    public router: Router,
    public authService: AuthService
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
    })
  }

  deleteDiet(row: any) {
    this.dietService.delete(row.id).subscribe(data => {
      this.refresh();
    })
  }

  getAllDiets() {
    this.dietService.getDiets().subscribe(data => {
      this.dataSource = this.dataSource.concat(data);
    })
  }

  refresh(): void {
    window.location.reload();
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
