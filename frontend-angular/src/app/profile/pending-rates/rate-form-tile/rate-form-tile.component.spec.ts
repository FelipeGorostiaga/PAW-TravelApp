import {ComponentFixture, TestBed} from '@angular/core/testing';

import {RateFormTileComponent} from './rate-form-tile.component';

describe('RateFormTileComponent', () => {
    let component: RateFormTileComponent;
    let fixture: ComponentFixture<RateFormTileComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RateFormTileComponent]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(RateFormTileComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
