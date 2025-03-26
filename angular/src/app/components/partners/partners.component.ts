import { Component, OnDestroy, OnInit, inject, ElementRef, ViewChildren, QueryList } from '@angular/core'
import { environment } from '../../../environments/environment.development'
import { Partner } from '../../models'
import { PartnerService } from '../../services/partner.service'
import { Subscription } from 'rxjs'

@Component({
  selector: 'app-partners',
  standalone: false,
  templateUrl: './partners.component.html',
  styleUrls: ['./partners.component.scss']
})
export class PartnersComponent implements OnInit, OnDestroy {
  
  private partnerService = inject(PartnerService)
  private partnerStoresSubscription$!: Subscription
  protected partners!: Partner[]
  protected isLoading: Boolean = true

  private map: any = null
  private markers: any[] = []
  private placesService: any = null
  protected selectedPartner: Partner | null = null
  

  @ViewChildren('partnerItem') partnerItems!: QueryList<ElementRef>

  ngOnInit(): void {
    this.loadGoogleMapsScript()
    this.loadPartners()
  }

  loadGoogleMapsScript() {

    if (window.document.getElementById('google-maps-script')){
      this.initializeMap()
      return
    }
    
    const script = document.createElement('script')
    script.id = 'google-maps-script'
    script.src = `https://maps.googleapis.com/maps/api/js?key=${environment.googleMapsApiKey}&libraries=places`
    script.async = true
    script.defer = true
    document.head.appendChild(script)
    script.onload = () => {
      // console.log('Google Maps API loaded')
      this.initializeMap()
    }
  }


  initializeMap() {
    try {
      const mapElement = document.getElementById('map')
      if (!mapElement) {
        console.error('Map element not found')
        return
      }

      const defaultCenter = { lat: 1.3521, lng: 103.8198 }
      this.map = new google.maps.Map(mapElement, {
        zoom: 12,
        center: defaultCenter,
        mapTypeControl: true,
        streetViewControl: false,
        fullscreenControl: true,
        zoomControl: true
      })

      // console.log('Map initialized:', this.map)


      this.placesService = new google.maps.places.PlacesService(this.map)


      if (this.partners && this.partners.length > 0) {
        this.addMarkers()
      }

    } catch (error) {
      console.error('Error initializing Google Maps: ', error)
    }
  }

  loadPartners() {
    this.partnerStoresSubscription$ = this.partnerService.partnerStores$.subscribe({
      next: (data) => {
        this.partners = data
        // console.log('Partners loaded successfully:', this.partners)


        if (this.map) {
          this.addMarkers()
        }

        this.isLoading = false
      },
      error: (error) => {
        // console.log('Error loading partner stores:', error)
        this.isLoading = false
      }
    })
  }

  addMarkers() {

    if (!this.map) {
      console.warn('Map not initialized')
      return
    }


    this.clearMarkers()

    if (!this.partners || this.partners.length === 0) {
      return
    }


    const bounds = new google.maps.LatLngBounds()


    this.partners.forEach(partner => {
      const position = {
        lat: partner.latitude,
        lng: partner.longitude 
      }


      const marker = new google.maps.Marker({
        position,
        map: this.map,
        title: partner.name,
        animation: google.maps.Animation.DROP
      })


      this.markers.push(marker)


      marker.addListener('click', () => {
        this.selectPartner(partner)
        this.scrollToPartner(partner)
      })

      bounds.extend(position)
    })


    this.map.fitBounds(bounds)

    if (this.partners.length === 1) {
      this.map.setZoom(14)
    }
  }

  clearMarkers() {
    this.markers.forEach(marker => {
      marker.setMap(null)
    })

    this.markers = []
  }

  selectPartner(partner: Partner) {
    this.selectedPartner = partner

    const markerIndex = this.partners.findIndex(p => p.partnerStoreId === partner.partnerStoreId)

    if (markerIndex !== -1 && this.markers[markerIndex] && this.map) {
      const marker = this.markers[markerIndex]

      this.map.panTo(marker.getPosition())
      this.map.setZoom(18)
    }
  }

  scrollToPartner(partner: Partner) {
    setTimeout(() => {
      const index = this.partners.findIndex(p => p.partnerStoreId === partner.partnerStoreId)
      if (index !== -1 && this.partnerItems && this.partnerItems.length > index) {
        const element = this.partnerItems.toArray()[index].nativeElement
        element.scrollIntoView({ behavior: 'smooth', block: 'start' })
      }
    }, 100)
  }


  highlightMarker(partner: Partner) {
    const markerIndex = this.partners.findIndex(p => p.partnerStoreId === partner.partnerStoreId)
    
    if (markerIndex !== -1 && this.markers[markerIndex]) {
      const marker = this.markers[markerIndex]
      
      marker.setAnimation(google.maps.Animation.BOUNCE)
    }
  }


  resetMarkerHighlight(partner: Partner) {
    const markerIndex = this.partners.findIndex(p => p.partnerStoreId === partner.partnerStoreId)
    
    if (markerIndex !== -1 && this.markers[markerIndex]) {
      const marker = this.markers[markerIndex]
      
      marker.setAnimation(null)
    }
  }

  ngOnDestroy(): void {
    if (this.partnerStoresSubscription$) {
      this.partnerStoresSubscription$.unsubscribe()
    }
  }
}