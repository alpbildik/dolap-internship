import { Component, OnInit } from '@angular/core';
import { Product } from '../product';


@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

	products : Product[] = [
        {
            "images": [
                "http://localhost:8080/media//Beyaz Gömlek1",
                "http://localhost:8080/media//Beyaz Gömlek2"
            ],
            "price": "20",
            "description": "Beyaz Gömlek Marka 1",
            "id": 4,
            "title": "Beyaz Gömlek",
            "category": "Gömlek"
        },
        {
            "images": [
                "http://localhost:8080/media//Gri Elbise1",
                "http://localhost:8080/media//Gri Elbise2"
            ],
            "price": "150",
            "description": "Gri Elbise Marka 1",
            "id": 7,
            "title": "Gri Elbise",
            "category": "Elbise"
        },
        {
            "images": [
                "http://localhost:8080/media//Mavi Pantolon1"
            ],
            "price": "100",
            "description": "MAvi Pantolon Marka 1",
            "id": 10,
            "title": "Mavi Pantolon",
            "category": "Pantolon"
        }
    ]
  productTitle = 'Product Page';
  arr		   = [1,2,3,4]

  constructor() { }

  ngOnInit() {
  }


}
