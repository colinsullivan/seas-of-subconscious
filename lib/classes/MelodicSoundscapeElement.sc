/**
 *  @file       MelodicSoundscapeElement.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Melodic bass samples recorded from my bass guitar.
 **/
MelodicSoundscapeElement : SoundscapeElement {
  var <>soundscapeBufKeys, <>instrKeys, <>eventKeys;

  init {
    arg args;
    super.init(args);

    // reverb pretty wet
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -16.0.dbamp()
    );

    // far less dry signal
    this.outChannel.level = -26.0.dbamp();

    this.soundscapeBufKeys = [
      /*\avadhuta01Buf,
      \avadhuta02Buf,
      \descendingDissonantBuf,
      \triadsBuf*/
    ];

    this.instrKeys = [
      \bassSlide
    ];

    this.eventKeys = this.soundscapeBufKeys ++ this.instrKeys;

    this.transitionTime = 0.1;
    
    /*this.offTimeMin = 35.0;
    this.offTimeMax = 80.0;*/
    this.offTimeMin = 5.0;
    this.offTimeMax = 10.0;
  }

  create_next_patch {
    var bufKey,
      buf,
      eventKey;

    super.create_next_patch();

    eventKey = this.eventKeys.choose();

    if (this.soundscapeBufKeys.includes(eventKey), {
      buf = this.bufManager.bufs[eventKey];

      this.onTimeMin = buf.duration;
      this.onTimeMax = buf.duration;

      ("preparing melody: " ++ bufKey).postln();

      ^Patch("BassAutoGarble", (
        buf: buf,
        gate: KrNumberEditor.new(0, \gate.asSpec()),
        attackTime: this.transitionTime,
        releaseTime: this.transitionTime,
        convertToStereo: 1
      ));
      
    }, {
      if (eventKey == \bassSlide, {
        ("preparing bass slide").postln();

        this.onTimeMin = 4.0;
        this.onTimeMax = this.onTimeMin;

        ^Patch("BassSlide", (
          gate: KrNumberEditor.new(0, \gate.asSpec()),
          duration: this.onTimeMin
        ));

      });
    });
  }
}
