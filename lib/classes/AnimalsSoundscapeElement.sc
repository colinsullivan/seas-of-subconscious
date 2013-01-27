AnimalsSoundscapeElement : SoundscapeElement {
  var <>animalBufs,
    <>ambientOnTimeMin,
    <>ambientOnTimeMax,
    <>loonsBufSections;

  init {
    arg args;

    super.init(args);

    // send animals through reverb fairly wet
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -5.0.dbamp()
    );

    this.outChannel.level = -12.0.dbamp();

    this.animalBufs = [\warblerBuf, \gullsBuf, \loonsBuf];

    this.loonsBufSections = [
      [0.0, 2.5],
      [2.5, 6.1],
      [6.1, 11.9],
      [11.9, 18.31],
      [18.31, 24.9],
      [24.9, 29.14],
      [29.14, 31.8],
      [56.0, 60],
      [77.0, 85.7],
      [86.0, 93.0]
    ];
    
    this.transitionTime = 2.0;
    
    this.offTimeMin = 5.0;
    this.offTimeMax = 19.0;

    this.ambientOnTimeMin = 5.0;
    this.ambientOnTimeMax = 12.0;
  
  }

  create_next_patch {


    var bufKey,
      startTime = 0,
      bufSegment;

    super.create_next_patch();

    bufKey = this.animalBufs.choose();
    /*bufKey = \loonsBuf;*/

    ("prepared animal: " ++ bufKey).postln();
    
    if (bufKey == \loonsBuf, { 
      // only play segment of loons buffer
      bufSegment = this.loonsBufSections.choose();
      this.onTimeMin = bufSegment[1] - bufSegment[0];
      this.onTimeMax = this.onTimeMin;
      startTime = bufSegment[0];
    }, {

      this.onTimeMin = this.ambientOnTimeMin;
      this.onTimeMax = this.ambientOnTimeMax;
    
    });

    ^Patch("cs.sfx.PlayBuf", (
      buf: this.soundscape.bufs[bufKey],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      startTime: startTime,
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime
    ));
  }

  play {

    super.play();

  
  }

}
