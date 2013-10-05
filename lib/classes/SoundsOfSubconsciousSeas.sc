/**
 *  @file       SoundsOfSubconsciousSeas.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Entry-point into soundscape.  Creates all elements and plays them.
 *  TODO: generalize this into a `Soundscape` class.
 **/
SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>reverbReturn,
    <>channels,
    <>elements,
    <>bufManager;

  init {
    arg params;
    var create_channel_to_mixer, test;

    this.bufManager = params[\bufManager];

    this.masterChannel = MixerChannel.new(
      \masterChannel,
      Server.default,
      2,
      2,
      2.4,
      outbus: 8
    );
    this.masterChannel.playfx(FxPatch("cs.fx.Notcher.notcher", (
      notchFreqs: [24000.0 ],
      notchrQs:   [0.1   ],
      notchdBs:   [-6.0  ],
      numChan: 2
    )));

    this.reverbReturn = MixerChannel.new(
      \reverbReturn,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.masterChannel
    );
    this.reverbReturn.playfx(FxPatch("cs.fx.Reverb.reverberator", (
      numChan: 2
    )));

    this.elements = (
      ambience: AmbienceElement.new(),
      animals: AnimalsSoundscapeElement.new(),
      creaks: CreakSoundscapeElement.new(),
      creepyBells: CreepyBellElement.new(),
      dreams: DreamsSoundscapeElement.new(),
      melodic: MelodicSoundscapeElement.new(),
      fogHorn: FogHornElement.new(),
      shanties: ShantiesSoundscapeElement.new()
    );

    // initialize all elements
    this.elements.keysValuesDo({
      arg key, element;

      element.init((
        soundscape: this,
        bufManager: this.bufManager,
        key: key
      ));
    });

  }

  prepare_soundscape {
    this.elements.do({
      arg element;

      element.prepare_to_play();
    });
  }

  start_soundscape {

    // play all elements
    this.elements.do({
      arg element;

      if (element != 0, {
        element.play();    
      });
    });
  
  }
}
