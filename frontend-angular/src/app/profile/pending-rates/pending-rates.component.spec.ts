import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PendingRatesComponent} from './pending-rates.component';

describe('PendingRatesComponent', () => {
    let component: PendingRatesComponent;
    let fixture: ComponentFixture<PendingRatesComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [PendingRatesComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(PendingRatesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
