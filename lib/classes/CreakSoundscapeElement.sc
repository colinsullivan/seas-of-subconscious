CreakSoundscapeElement : SoundscapeElement {

  var <>bufferStartingMarkers,
    <>nextCreakStartTime,
    <>nextCreakEndTime;

  init {
    arg args;

    super.init(args);

    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -6.0.dbamp()
    );

    this.outChannel.level = -10.0.dbamp();
    

    this.bufferStartingMarkers = [
      0.0,
      3.5,
      6.5,
      9.7,
      12.7,
      15.5,
      18.7,
      22.1,
      25.4,
      29.2,
      32.2,
      35.5,
      38.9,
      41.9,
      44.8,
      48.3,
      51.6,
      54.6,
      57.5
    ];
    this.nextCreakStartTime = nil;
    this.nextCreakEndTime = nil;
    
    this.offTimeMin = 10.0;
    this.offTimeMax = 25.0;
    
    this.transitionTime = 0.1;
  
  }

  create_next_patch {
    var nextCreakIndex;

    super.create_next_patch();
        
    "prepare_next_creak".postln;

    nextCreakIndex = (this.bufferStartingMarkers.size - 2).rand();
    this.nextCreakStartTime = this.bufferStartingMarkers[nextCreakIndex];
    this.nextCreakEndTime = this.bufferStartingMarkers[nextCreakIndex + 1];
    this.onTimeMax = this.nextCreakEndTime - this.nextCreakStartTime;
    this.onTimeMin = this.onTimeMax;
    
    ^Patch("cs.sfx.PlayBuf", (
      buf: this.soundscape.bufs[\creakingFloorboardBuf],
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      startTime: this.nextCreakStartTime,
      convertToStereo: 1
    ));
  }


}
