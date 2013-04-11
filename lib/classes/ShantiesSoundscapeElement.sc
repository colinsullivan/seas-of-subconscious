/**
 *  @file       ShantiesSoundscapeElement.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Sea shanty samples.
 **/
ShantiesSoundscapeElement : SoundscapeElement {

  var <>bufNames,
    <>bufSections,
    <>filterPatch;

  init {
    arg args;

    super.init(args);

    // filter voice
    /*this.filterPatch = FxPatch("cs.fx.HPFerModulated", (*/
      /*numChan: 2,*/
      /*cutoffMinFreq: 800,*/
      /*cutoffMaxFreq: 8000,*/
      /*cutoffModFreq: 0.3*/
    /*));*/


    // send voice through reverb
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -16.0.dbamp()
    );

    this.outChannel.level = -22.0.dbamp();

    this.bufNames = [\seaShanty01, \seaShanty02, \seaShanty03, \seaShanty04];

    /*this.bufSections = (*/
      /*derbyshireRunningBuf: [*/
        /*[65.5, 68.2],*/
        /*[68, 70],*/
        /*[87.2, 89.8],*/
        /*[91.8, 104.1],*/
        /*[104.1, 106.3],*/
        /*[113.2, 115.9],*/
        /*[117.9, 121.2],*/
        /*[121.7, 124.6]*/
      /*]*/
    /*);*/

    this.transitionTime = 2.0;

    this.offTimeMin = 10.0;
    this.offTimeMax = 50.0;
    
    this.onTimeMin = 10.0;
    this.onTimeMax = 20.0;
  }

  create_next_patch {
    var bufKey,
      bufSection,
      buf;

    super.create_next_patch();

    bufKey = this.bufNames.choose();
    buf = this.soundscape.bufs[bufKey];
    /*bufSection = this.bufSections[bufKey].choose();*/

    ("preparing shanty: " ++ bufKey /*++ " - " ++ bufSection*/).postln();

    ^Patch("cs.sfx.PlayBufWarped", (
      buf: buf,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      playbackRate: rrand(0.8, 0.9),
      warpAmp: 0.05,
      warpFreq: 0.2,
      startTime: (buf.duration() - this.onTimeMax).rand(),
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime
    ));
  }

  /*play {*/
    /*super.play();*/

    /*this.outChannel.playfx(this.filterPatch);*/
  /*}*/

}
